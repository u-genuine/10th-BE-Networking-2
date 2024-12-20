package cotato.backend.domains.post;

import org.hibernate.annotations.ColumnDefault;

import cotato.backend.domains.post.dto.request.SavePostRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private String name;

	private Integer views;

	private Integer likes;

	@Builder
	public Post(String title, String content, String name) {
		this.title = title;
		this.content = content;
		this.name = name;
		this.views = 0;
		this.likes = 0;
	}

	public static Post toPost(SavePostRequest postDTO){
		return Post.builder()
			.title(postDTO.getTitle())
			.content(postDTO.getContent())
			.name(postDTO.getName())
			.build();
	}
}
