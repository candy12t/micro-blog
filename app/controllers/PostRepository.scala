package controllers

import scalikejdbc._
import models.Post

object PostRepository {

  def post_list: Seq[Post] = DB readOnly { implicit sesssion =>
    sql"""SELECT id, user_id, text, parent_post_id, comment_count, posted_at
          FROM posts
          ORDER by posted_at DESC
       """
      .map { rs =>
        Post(
          rs.string("id"),
          rs.string("user_id"),
          rs.string("text"),
          rs.string("parent_post_id"),
          rs.int("comment_count"),
          rs.offsetDateTime("posted_at")
        )
      }
      .list()
      .apply()
  }

  def post_add(post: Post): Unit = DB localTx { implicit session =>
    sql"""INSERT INTO posts (id, user_id, text, posted_at)
          VALUES (${post.id}, ${post.user_id}, ${post.text}, ${post.posted_at})
       """
      .update()
      .apply()
  }
}
