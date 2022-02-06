package controllers

import models.{Product, User}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json._
import play.api.mvc._
import services.CoupangService
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile._

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class CoupangController @Inject()
  (protected val dbConfigProvider: DatabaseConfigProvider, cc: ControllerComponents)
  (implicit ec: ExecutionContext)
  extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {

  private val service = new CoupangService(db)

  implicit val productsRowReads: Reads[Product] = Json.reads[Product]
  implicit val productRowWrites: OWrites[Product] = Json.writes[Product]
  def getProduct(id: Option[Int]): Action[AnyContent] = Action.async { implicit request =>
    service.getProducts(id).map(product => Ok(Json.toJson(product)))
  }
  def createProduct(name: String, url: String): Action[AnyContent] = Action.async { implicit request =>
    service.createProduct(name, url).map {
      case 1 => Created(s"Created product: ${name}\n(${url})")
      case others => NotAcceptable(s"Cannot create Product: ${name}\n(${url})")
    }
  }
  def removeProduct(id: Int): Action[AnyContent] = Action.async { implicit request =>
    service.removeProduct(id).map {
      case 1 => Accepted(s"Removed product with id: ${id}")
      case _ => NotAcceptable(s"Cannot remove product with id: ${id}")
    }
  }

  implicit val usersRowReads: Reads[User] = Json.reads[User]
  implicit val usersRowWrites: OWrites[User] = Json.writes[User]
  def getUserByName(name: String): Action[AnyContent] = Action.async { implicit request =>
    service
      .getUserByName(name)
      .map(user => Ok(Json.toJson(user)))
  }

}
