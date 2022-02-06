package controllers

import models.{Basket, Price, Product, User}
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


  implicit val productRowWrites: OWrites[Product] = Json.writes[Product]

  def getProduct(id: Option[Int]): Action[AnyContent] = Action.async { implicit request =>
    service.getProducts(id).map(product => Ok(Json.toJson(product)))
  }

  implicit val userRowWrites: OWrites[User] = Json.writes[User]
  implicit val priceRowWrites: OWrites[Price] = Json.writes[Price]
  implicit val basketRowWrites: OWrites[Basket] = Json.writes[Basket]

  def removeProduct(id: Int): Action[AnyContent] = Action.async { implicit request =>
    service.removeProduct(id).map {
      case 1 => Accepted(s"Removed product with id: $id")
      case _ => NotAcceptable(s"Cannot remove product with id: $id")
    }
  }

  def createProduct(name: String, url: String): Action[AnyContent] = Action.async { implicit request =>
    service.createProduct(name, url).map {
      case 1 => Created(s"Created product: $name\n($url)")
      case others => NotAcceptable(s"Cannot create Product: $name\n($url)")
    }
  }

  def getUserByName(name: String): Action[AnyContent] = Action.async { implicit request =>
    service.getUserByName(name).map(user => Ok(Json.toJson(user)))
  }

  def createUser(name: String): Action[AnyContent] = Action.async { implicit request =>
    service.createUser(name).map {
      case 1 => Created(s"Created User: $name")
      case others => NotAcceptable(s"Cannot create user: $name")
    }
  }

  def removeUser(id: Int): Action[AnyContent] = Action.async { implicit request =>
    service.removeProduct(id).map {
      case 1 => Accepted(s"Removed user with id: $id")
      case _ => NotAcceptable(s"Cannot remove user with id: $id")
    }
  }

  def getPricesByProdId(id: Int): Action[AnyContent] = Action.async { implicit request =>
    service.getPricesByProdId(id).map(price => Ok(Json.toJson(price)))
  }

  def addPriceOfProductByProdId(id: Int, price: Long): Action[AnyContent] = Action.async { implicit request =>
    service.addPriceOfProductByProdId(id, price).map {
      case 1 => Created(s"Price appended - product: $id, price: $price")
      case others => NotAcceptable(s"Cannot add product price - product: $id")
    }
  }

  def removeDataBeforeTime(date: String): Action[AnyContent] = Action.async {
    service.removeDataBeforeDate(date).map {
      case 1 => Accepted(s"Removed data before $date")
      case _ => NotAcceptable(s"Cannot remove data before $date")
    }
  }

  def getBasketByUserId(id: Int): Action[AnyContent] = Action.async { implicit request =>
    service.getBasketByUserId(id).map(basket => Ok(Json.toJson(basket)))
  }

  def addProductToBasket(prodId: Int, userId: Int): Action[AnyContent] = Action.async { implicit request =>
    service.addProductToBasket(prodId, userId).map {
      case 1 => Created(s"Product added to basket - product: $prodId, user: $userId")
      case others => NotAcceptable(s"Cannot add product to basket - product: $prodId,, user: $userId")
    }
  }

  def removeProductFromBasket(prodId: Int, userId: Int): Action[AnyContent] = Action.async { implicit request =>
    service.removeProductFromBasket(prodId, userId).map {
      case 1 => Accepted(s"Removed product from basket - product: $prodId, user: $userId")
      case _ => NotAcceptable(s"Cannot remove product from basket - product: $prodId, user: $userId")
    }
  }


}
