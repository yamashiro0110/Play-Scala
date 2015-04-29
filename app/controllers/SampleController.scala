package controllers

import models.User
import play.api.mvc.{Action, Controller}

/**
 * Created by yamashiro-r on 15/04/20.
 */
object SampleController extends Controller {

  def index = Action {
    val usr = User.findById(1).getOrElse(User.notFound)
    Ok("Call SampleController index!! -> " + usr.name)
  }

}
