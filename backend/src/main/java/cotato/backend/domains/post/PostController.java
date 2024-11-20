package cotato.backend.domains.post;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cotato.backend.common.dto.DataResponse;
import cotato.backend.domains.post.dto.request.SavePostRequest;
import cotato.backend.domains.post.dto.request.SavePostsByExcelRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@PostMapping("/excel")
	public ResponseEntity<DataResponse<Void>> savePostsByExcel(@RequestBody SavePostsByExcelRequest request) {
		postService.saveEstatesByExcel(request.getPath());

		return ResponseEntity.ok(DataResponse.ok());
	}

	@PostMapping("/")
	@Operation(summary = "게시글 단일 생성 API")
	public ResponseEntity<DataResponse<Void>> savePost(@RequestBody SavePostRequest request){
		postService.savePost(request);

		return ResponseEntity.ok(DataResponse.ok());
	}
}
