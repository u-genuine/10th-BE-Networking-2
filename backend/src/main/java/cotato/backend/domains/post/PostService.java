package cotato.backend.domains.post;

import static cotato.backend.common.exception.ErrorCode.*;

import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cotato.backend.common.excel.ExcelUtils;
import cotato.backend.common.exception.ApiException;
import cotato.backend.common.exception.ErrorCode;
import cotato.backend.domains.post.dto.request.SavePostRequest;
import cotato.backend.domains.post.dto.response.PostDetailResponse;
import cotato.backend.domains.post.dto.response.PostListResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional
public class PostService {

	private final PostRepository postRepository;

	// 로컬 파일 경로로부터 엑셀 파일을 읽어 Post 엔터티로 변환하고 저장
	public void saveEstatesByExcel(String filePath) {
		try {

			// 엑셀 파일을 읽어 데이터 프레임 형태로 변환
			int batchSize = 5000; // 배치 크기 설정
			List<Post> posts = ExcelUtils.parseExcelFile(filePath).stream()
				.map(row -> {
					String title = row.get("title");
					String content = row.get("content");
					String name = row.get("name");

					return new Post(title, content, name);
				})
				.collect(Collectors.toList());

			for(int i = 0; i < posts.size(); i+= batchSize) {
				List<Post> batch = posts.subList(i, Math.min(posts.size(), i + batchSize));
				postRepository.saveAll(batch);
			}

		} catch (Exception e) {
			log.error("Failed to save estates by excel", e);
			throw ApiException.from(INTERNAL_SERVER_ERROR);
		}
	}

	public void savePost(SavePostRequest request){
		try{
			Post post = new Post(request.getTitle(), request.getContent(), request.getName());
			postRepository.save(post);

		}catch (Exception e){
			log.error("Failed to save post", e);
			throw ApiException.from(INTERNAL_SERVER_ERROR);
		}
	}

	public PostDetailResponse getPost(Long postId){
		try {
			Post post = postRepository.findById(postId)
				.orElseThrow(() -> ApiException.from(ErrorCode.POST_NOT_FOUND));

			post.setViews(post.getViews() + 1);
			postRepository.save(post);

			return PostDetailResponse.builder()
				.title(post.getTitle())
				.content(post.getContent())
				.name(post.getName())
				.views(post.getViews())
				.build();

		} catch (Exception e){
			log.error("Failed to get the post", e);
			throw ApiException.from(INTERNAL_SERVER_ERROR);
		}
	}

	public PostListResponse.PostPreviewList getPostList(Integer page){
		try {
			PageRequest pageRequest = PageRequest.of(page -1 , 10, Sort.by("likes").descending());
			Page<Post> postPage = postRepository.findAll(pageRequest);

			List<PostListResponse.PostPreview> postPreviewList = postPage.stream()
				.map(post -> PostListResponse.PostPreview.builder()
					.id(post.getId())
					.title(post.getTitle())
					.name(post.getName())
					.likes(post.getLikes())
					.build()
				).collect(Collectors.toList());

			return PostListResponse.PostPreviewList.builder()
				.currentPage(postPage.getNumber() + 1)
				.totalPage(postPage.getTotalPages())
				.postList(postPreviewList)
				.build();

		}catch (Exception e){
			log.error("Failed to get the post list", e);
			throw ApiException.from(INTERNAL_SERVER_ERROR);
		}
	}

	public void deletePost(Long postId){
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> ApiException.from(ErrorCode.POST_NOT_FOUND));
		postRepository.delete(post);
	}

}
