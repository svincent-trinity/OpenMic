package models

object CodeGen extends App {
  slick.codegen.SourceCodeGenerator.run(
    "slick.jdbc.PostgresProfile", 
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost/openmic?user=samvincent&password=password",
    "/Users/samvincent/Desktop/FinPro/iAudition/server/app/",
    "models", None, None, true, false
  )
}