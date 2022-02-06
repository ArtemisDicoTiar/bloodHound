package repositories
// AUTO-GENERATED Slick data model
/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Baskets.schema ++ Prices.schema ++ Products.schema ++ Users.schema

  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl: profile.DDL = schema

  /** Entity class storing rows of table Baskets
   *
   * @param userid Database column userId SqlType(INT)
   * @param prodid Database column prodId SqlType(INT) */
  case class BasketsRow(userid: Int, prodid: Int)

  /** GetResult implicit for fetching BasketsRow objects using plain SQL queries */
  implicit def GetResultBasketsRow(implicit e0: GR[Int]): GR[BasketsRow] = GR {
    prs =>
      import prs._
      BasketsRow.tupled((<<[Int], <<[Int]))
  }

  /** Table description of table baskets. Objects of this class serve as prototypes for rows in queries. */
  class Baskets(_tableTag: Tag) extends profile.api.Table[BasketsRow](_tableTag, Some("coupang"), "baskets") {
    def * = (userid, prodid) <> (BasketsRow.tupled, BasketsRow.unapply)

    /** Foreign key referencing Products (database name productId) */
    lazy val productsFk = foreignKey("productId", prodid, Products)(r => r.prodid, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Restrict)
    /** Database column userId SqlType(INT) */
    val userid: Rep[Int] = column[Int]("userId")
    /** Database column prodId SqlType(INT) */
    val prodid: Rep[Int] = column[Int]("prodId")

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(userid), Rep.Some(prodid)).shaped.<>({ r => import r._; _1.map(_ => BasketsRow.tupled((_1.get, _2.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }
  /** Collection-like TableQuery object for table Baskets */
  lazy val Baskets = new TableQuery(tag => new Baskets(tag))

  /** Entity class storing rows of table Prices
   *  @param prodid Database column prodId SqlType(INT)
   *  @param datetime Database column datetime SqlType(DATETIME)
   *  @param price Database column price SqlType(BIGINT) */
  case class PricesRow(prodid: Int, datetime: java.sql.Timestamp, price: Long)
  /** GetResult implicit for fetching PricesRow objects using plain SQL queries */
  implicit def GetResultPricesRow(implicit e0: GR[Int], e1: GR[java.sql.Timestamp], e2: GR[Long]): GR[PricesRow] = GR{
    prs => import prs._
    PricesRow.tupled((<<[Int], <<[java.sql.Timestamp], <<[Long]))
  }
  /** Table description of table prices. Objects of this class serve as prototypes for rows in queries. */
  class Prices(_tableTag: Tag) extends profile.api.Table[PricesRow](_tableTag, Some("coupang"), "prices") {
    def * = (prodid, datetime, price) <> (PricesRow.tupled, PricesRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(prodid), Rep.Some(datetime), Rep.Some(price)).shaped.<>({ r => import r._; _1.map(_ => PricesRow.tupled((_1.get, _2.get, _3.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column prodId SqlType(INT) */
    val prodid: Rep[Int] = column[Int]("prodId")
    /** Database column datetime SqlType(DATETIME) */
    val datetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("datetime")
    /** Database column price SqlType(BIGINT) */
    val price: Rep[Long] = column[Long]("price")

    /** Foreign key referencing Products (database name prodId) */
    lazy val productsFk = foreignKey("prodId", prodid, Products)(r => r.prodid, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table Prices */
  lazy val Prices = new TableQuery(tag => new Prices(tag))

  /** Entity class storing rows of table Products
   *  @param prodid Database column prodId SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(200,true)
   *  @param url Database column url SqlType(VARCHAR), Length(500,true) */
  case class ProductsRow(prodid: Int, name: String, url: String)
  /** GetResult implicit for fetching ProductsRow objects using plain SQL queries */
  implicit def GetResultProductsRow(implicit e0: GR[Int], e1: GR[String]): GR[ProductsRow] = GR{
    prs => import prs._
    ProductsRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table products. Objects of this class serve as prototypes for rows in queries. */
  class Products(_tableTag: Tag) extends profile.api.Table[ProductsRow](_tableTag, Some("coupang"), "products") {
    def * = (prodid, name, url) <> (ProductsRow.tupled, ProductsRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(prodid), Rep.Some(name), Rep.Some(url)).shaped.<>({ r => import r._; _1.map(_ => ProductsRow.tupled((_1.get, _2.get, _3.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column prodId SqlType(INT), AutoInc, PrimaryKey */
    val prodid: Rep[Int] = column[Int]("prodId", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(200,true) */
    val name: Rep[String] = column[String]("name", O.Length(200,varying=true))
    /** Database column url SqlType(VARCHAR), Length(500,true) */
    val url: Rep[String] = column[String]("url", O.Length(500,varying=true))

    /** Index over (name) (database name products_name_index) */
    val index1 = index("products_name_index", name)
    /** Uniqueness Index over (url) (database name products_url_uindex) */
    val index2 = index("products_url_uindex", url, unique=true)
  }
  /** Collection-like TableQuery object for table Products */
  lazy val Products = new TableQuery(tag => new Products(tag))

  /** Entity class storing rows of table Users
   *  @param userid Database column userId SqlType(INT), AutoInc, PrimaryKey
   *  @param username Database column userName SqlType(VARCHAR), Length(20,true) */
  case class UsersRow(userid: Int, username: String)
  /** GetResult implicit for fetching UsersRow objects using plain SQL queries */
  implicit def GetResultUsersRow(implicit e0: GR[Int], e1: GR[String]): GR[UsersRow] = GR{
    prs => import prs._
    UsersRow.tupled((<<[Int], <<[String]))
  }
  /** Table description of table users. Objects of this class serve as prototypes for rows in queries. */
  class Users(_tableTag: Tag) extends profile.api.Table[UsersRow](_tableTag, Some("coupang"), "users") {
    def * = (userid, username) <> (UsersRow.tupled, UsersRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(userid), Rep.Some(username)).shaped.<>({ r => import r._; _1.map(_ => UsersRow.tupled((_1.get, _2.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column userId SqlType(INT), AutoInc, PrimaryKey */
    val userid: Rep[Int] = column[Int]("userId", O.AutoInc, O.PrimaryKey)
    /** Database column userName SqlType(VARCHAR), Length(20,true) */
    val username: Rep[String] = column[String]("userName", O.Length(20, varying = true))
  }

  /** Collection-like TableQuery object for table Users */
  lazy val Users = new TableQuery(tag => new Users(tag))
}

/** Stand-alone Slick data model for immediate use */
object Tables extends slick.jdbc.MySQLProfile with Tables
