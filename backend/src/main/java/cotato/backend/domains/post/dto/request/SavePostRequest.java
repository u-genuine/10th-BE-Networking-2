package cotato.backend.domains.post.dto.request;

import lombok.Getter;

@Getter
public class SavePostRequest {
	private String title;
	private String content;
	private String name;
}
