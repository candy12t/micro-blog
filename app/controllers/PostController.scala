package controllers

import javax.inject.Inject
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._
import java.time.OffsetDateTime
import models.Post
import java.util.UUID

case class PostForm(user_id: String, text: String)

class PostController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  private val form = Form(
    mapping(
      "user_id" -> nonEmptyText,
      "text" -> nonEmptyText(maxLength = 100)
    )(PostForm.apply)(PostForm.unapply)
  )

  def list() = Action { implicit request =>
    Ok(Json.obj("posts" -> Json.toJson(PostRepository.post_list)))
  }

  def create() = Action { implicit request =>
    form.bindFromRequest.fold(
      error => {
        BadRequest(Json.obj("result" -> "NG", "message" -> "textの長さが1文字以上100以下ではありません"))
      },
      postForm => {
        val uuid = UUID.randomUUID().toString()
        val post = Post(uuid, postForm.user_id, postForm.text, OffsetDateTime.now)
        try {
          PostRepository.post_add(post)
          Ok(Json.obj("result" -> "OK"))
        } catch {
          case e => BadRequest(Json.obj("result" -> "NG", "message" -> "user_idが正しくありません"))
        }
      }
    )
  }
}
