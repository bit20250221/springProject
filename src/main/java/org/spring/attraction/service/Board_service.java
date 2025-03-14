package org.spring.attraction.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.spring.attraction.ENUM.Tab;
import org.spring.attraction.dto.BoardImage_dto;
import org.spring.attraction.dto.Board_dto;
import org.spring.attraction.dto.UserDto;
import org.spring.attraction.entity.Attraction;
import org.spring.attraction.entity.Board;
import org.spring.attraction.entity.Comment;
import org.spring.attraction.entity.User;
import org.spring.attraction.repository.AttractionRepository;
import org.spring.attraction.repository.Board_repository;
import org.spring.attraction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class Board_service {

    @Autowired
    public Board_repository repository;

    @Autowired
    public BoardImage_service boardImageService;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public Board_SecurityService securityService;

    @Autowired
    public AttractionRepository attractionRepository;

    @Autowired
    public Comment_service commentService;

    //게시글 한개 읽기, 댓글은 따로 처리
    @Transactional
    public Board_dto getBoard(Long Board_id){
        Optional<Board> board=repository.findById(Board_id);
        Board_dto boardDto;
        if(board.isPresent()){
            Board exboard=board.get();
            Long user_id=exboard.getUser().getId();
            Long attraction_id=null;
            if(exboard.getAttraction()!=null) {
                attraction_id = exboard.getAttraction().getId();
            }
            List<BoardImage_dto> boardImages=boardImageService.getImagesByBoardId(exboard.getId());
            boardDto=Board_dto.to_dto_2(exboard,user_id,attraction_id);
            boardDto.setBoardImages(boardImages);
        }else{
            boardDto=null;
        }
        return boardDto;
    }

    //게시글 전체 리스트 읽어오기(게시글 아이디, 게시글)
    @Transactional
    public HashMap<Long,Board_dto> getBoardList(){
        List<Board> boards=repository.findAll();
        HashMap<Long,Board_dto> dtoMap=new HashMap<>();

        if(!boards.isEmpty()){
            for(Board entity: boards){
                Long user_id=entity.getUser().getId();
                Long attraction_id=null;
                if(entity.getAttraction()!=null) {
                    attraction_id = entity.getAttraction().getId();
                }
                Board_dto dto=Board_dto.to_dto_2(entity,user_id,attraction_id);
                dtoMap.put(entity.getId(),dto);
            }

            return dtoMap;
        }else{
            return null;
        }
    }

    //게시판 검색(페이징 처리 제외하고 전체 목록을 가져옴)
    public HashMap<Long,Board_dto> getSearchBoard(String tab, String type, String keyword){
        List<Board> boards=null;
        HashMap<Long,Board_dto> dtoMap =new HashMap<>();
        switch (type){
            case "title":
                boards=repository.boardTitleSearch(Tab.valueOf(tab),keyword);
                break;
            case "content":
                boards=repository.boardContentSearch(Tab.valueOf(tab),keyword);
                break;
            case "writer":
                boards=repository.boardWriterSearch(Tab.valueOf(tab),keyword);
                break;
        }
        if(!boards.isEmpty()){
            for(Board entity:boards){
                if(entity.getAttraction()!=null) {
                    Board_dto dto = Board_dto.to_dto_2(entity, entity.getUser().getId(), entity.getAttraction().getId());
                    dtoMap.put(entity.getId(), dto);
                }else{
                    Board_dto dto=Board_dto.to_dto_2(entity,entity.getUser().getId(),null);
                    dtoMap.put(entity.getId(), dto);
                }
            }
            return dtoMap;
        }else{
            return null;
        }
    }

    //게시판 검색(페이징 처리 포함)
    @Transactional
    public Page<Board_dto> getSearchPageBoard(String tab,  String type, String keyword, int pageNum, int pageAmount){
        List<Sort.Order> sorts=new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable= PageRequest.of(pageNum,pageAmount,Sort.by(sorts));

        Page<Board> entityPage;
        if(!tab.isEmpty() && !type.isEmpty() && !keyword.isEmpty()) {
            //탭과 타입, 키워드 셋 다 존재
            entityPage = switch (type) {
                case "title" -> repository.boardTitleSearch(Tab.valueOf(tab), keyword, pageable);
                case "content" -> repository.boardContentSearch(Tab.valueOf(tab), keyword, pageable);
                case "writer" -> repository.boardWriterSearch(Tab.valueOf(tab), keyword, pageable);
                default -> null;
            };
        }else if(tab.isEmpty() && !type.isEmpty() && !keyword.isEmpty()){
            //타입, 키워드만 존재
            entityPage= switch (type) {
                case "title" -> repository.findByTitleContaining(keyword, pageable);
                case "content" -> repository.findByContentContaining(keyword, pageable);
                case "writer" -> repository.findByWriterIdContaining(keyword, pageable);
                default -> null;
            };
        }else if(!tab.isEmpty() && type.isEmpty() && !keyword.isEmpty()){
            //탭, 키워드만 존재
            entityPage=repository.findByTabSearch(Tab.valueOf(tab), keyword, pageable);
        }
        else if(!tab.isEmpty() && type.isEmpty() && keyword.isEmpty()){
            //탭만 존재
            entityPage=repository.findByTab(Tab.valueOf(tab),pageable);
        }else if(tab.isEmpty() && type.isEmpty() && !keyword.isEmpty()){
            //키워드만 존재
            entityPage=repository.findByAllKeyword(keyword,pageable);
        }
        else{
            //조건 없음(전체)
            entityPage=repository.findAll(pageable);
        }
        return Objects.requireNonNull(entityPage).map(
                entity-> Board_dto.to_dto_2(
                        entity,
                        entity.getUser().getId(),
                        entity.getAttraction() != null ? entity.getAttraction().getId() : null)
            );
    }

    /*
    게시글 쓰기(유저의 아이디, 관광지 아이디 포함), 초기 게시글 생성시 댓글 0개
    나중에 유저 DTO, 관광지 DTO 인수로 받아야한다.
    */
    @Transactional
    public Board writeBoard(Board_dto write){

        //나중에 따로 수정
        //User user=new User();
        Optional<User> optionalUser=userRepository.findById(write.getUser_id());
        User user;
        if(optionalUser.isPresent()) {
            user = optionalUser.get();
        }else{
            return null;
        }
        Board entity;
        write.setCreartedate(LocalDateTime.now());
        if(write.getAttraction_id()!=null) {
            //나중에 따로 수정
            Attraction attraction=attractionRepository.findById(write.getAttraction_id()).orElse(null);
            entity = toEntity(write,user,attraction,null);
        }
        else if(write.getAttraction_Name()!=null) {
            //연관 관계없이 게시판 상으로만 출력(DB에 없는 관광지)
            entity = toEntity(write,user,null,null);
        }
        else{
            entity = toEntity(write,user,null,null);
        }

        try {
            Board savedEntity=repository.save(entity);
            return savedEntity;
        }catch (Exception e){
            System.out.println("게시글 저장에 실패했습니다.");
            e.printStackTrace();
            return null;
        }
    }

    //게시글 삭제
    public boolean deleteBoard(Long id){
        Optional<Board> isExist=repository.findById(id);
        UserDto user= securityService.getUser();

        try {
            if (isExist.isPresent()) {
                if((isExist.get().getUser().getUserLoginId().compareTo(user.getUserLoginId())!=0)
                        || user.getUserType().name().compareTo("manager")!=0){
                    return false;
                }

                List<BoardImage_dto> imageList=boardImageService.getImagesByBoardId(id);
                if(imageList!=null) {
                    for(BoardImage_dto image:imageList){
                        boardImageService.deleteImageFile(image.getUUID(), image.getUUIDName(), id);
                    }
                }

                commentService.deleteCommentByBoardId(id);
                repository.deleteById(id);
                return true;
            } else {
                System.out.println("게시글이 존재하지 않습니다.");
                return false;
            }
        }catch (Exception e){
            System.out.println("게시글 삭제도중 오류 발생");
            e.printStackTrace();
            return false;
        }

    }

    //게시글 수정(관광지 수정은 불가능하도록),(이미지 파일 수정은 별개로 처리)
    public Board_dto updateBoard(Board_dto update){
        Optional<Board> isExist=repository.findById(update.getBoard_id());
        try{
            if(isExist.isPresent()){
                Attraction attraction;
                if(update.getAttraction_id()!=null){
                    attraction=attractionRepository.findById(update.getAttraction_id()).get();
                }else if(isExist.get().getAttraction()!=null){
                    attraction=attractionRepository.findById(isExist.get().getAttraction().getId()).get();
                }else{
                    attraction=null;
                }

                Board entity= Board_service.toEntity(update,userRepository.getReferenceById(update.getUser_id()),
                        attraction, null);
                Board newupdate=repository.save(entity);
                return Board_dto.to_dto_2(newupdate,newupdate.getUser().getId(), newupdate.getAttraction()!=null? newupdate.getAttraction().getId() : null);
            }else{
                System.out.println("게시글이 존재하지 않습니다.");
                return null;
            }
        }catch(Exception e){
            System.out.println("This is Error");
            e.printStackTrace();
            System.out.println("게시글 수정 처리중 오류가 발생하였습니다.");
            return null;
        }
    }

    //Board_dto를 받아서 엔티티로 변환시키는 메소드(단순 변환)
    public static Board toEntity(Board_dto dto, User user, Attraction attraction, List<Comment> comments){
        Board entity=new Board();
        if(dto.getBoard_id()!=null){
            entity.setId(dto.getBoard_id());
        }
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setCreateDate(dto.getCreartedate());
        entity.setUpdateDate(dto.getUpdatedate());
        entity.setTab(Tab.valueOf(dto.getTab()));
        if(dto.getRate()!=null) {
            entity.setRate(dto.getRate());
        }
        entity.setUser(user);
        entity.setAttraction(attraction);
        entity.setComments(comments);
        return entity;
    }
}
