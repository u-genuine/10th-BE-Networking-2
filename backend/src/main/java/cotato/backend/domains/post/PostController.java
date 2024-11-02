package cotato.backend.domains.post;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cotato.backend.common.dto.DataResponse;
import cotato.backend.domains.post.dto.request.SavePostRequest;
import cotato.backend.domains.post.dto.request.SavePostsByExcelRequest;
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

	@PostMapping
	public ResponseEntity<DataResponse<Void>> savePost(@RequestBody SavePostRequest request) {
		postService.save(request);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@GetMapping("/{postId}")
	public ResponseEntity<DataResponse<Void>> findPostById(@RequestParam Long postId) {
		postService.findPostById(postId);

		return ResponseEntity.ok(DataResponse.ok());
	}
}
