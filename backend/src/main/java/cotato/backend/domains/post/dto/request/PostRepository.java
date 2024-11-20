package cotato.backend.domains.post.dto.request;

import org.springframework.data.jpa.repository.JpaRepository;

import cotato.backend.domains.post.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
