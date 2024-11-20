package cotato.backend.domains.post.dto.request;

import lombok.Getter;

@Getter
public class SavePostRequest {
	String title;
	String content;
	String name;
}
