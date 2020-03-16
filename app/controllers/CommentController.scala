package controllers

import javax.inject.Inject
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._
import java.time.OffsetDateTime
import models.Comment
import java.util.UUID

case class CommentForm(user_id: String, text: String)

class CommentController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  private val form = Form(
    mapping(
      "user_id" -> nonEmptyText,
      "text" -> nonEmptyText(maxLength = 100)
    )(CommentForm.apply)(CommentForm.unapply)
  )

  def comment_list(post_id: String) = Action { implicit request =>
    Ok(Json.obj("comments" -> Json.toJson(CommentRepository.comment_list(post_id))))
  }

  def comment_create(post_id: String) = Action { implicit request =>
    form.bindFromRequest.fold(
      error => {
        BadRequest(Json.obj("result" -> "NG", "message" -> "textの長さが1文字以上100以下ではありません"))
      },
      commentForm => {
        val uuid = UUID.randomUUID().toString()
        val comment =
          Comment(uuid, commentForm.user_id, commentForm.text, post_id, OffsetDateTime.now)
        try {
          CommentRepository.comment_add(comment)
          CommentRepository.comment_count(post_id)
          Ok(Json.obj("result" -> "OK"))
        } catch {
          case e =>
            BadRequest(Json.obj("result" -> "NG", "message" -> "user_idまたはpost_idが正しくありません"))
        }
      }
    )
  }
}
