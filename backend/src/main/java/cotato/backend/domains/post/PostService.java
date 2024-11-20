package cotato.backend.domains.post;

import static cotato.backend.common.exception.ErrorCode.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cotato.backend.common.excel.ExcelUtils;
import cotato.backend.common.exception.ApiException;
import cotato.backend.domains.post.dto.request.PostRepository;
import cotato.backend.domains.post.dto.request.SavePostRequest;
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
}
