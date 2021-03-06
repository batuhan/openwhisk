/*
 * Copyright 2015-2016 IBM Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package whisk.core.controller.test

import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterEach
import org.scalatest.junit.JUnitRunner

import spray.http.StatusCodes._
import spray.http.Uri
import spray.httpx.SprayJsonSupport.sprayJsonUnmarshaller
import spray.json._
import spray.json.DefaultJsonProtocol._
import whisk.core.controller.SwaggerDocs

/**
 * Tests swagger routes.
 *
 * @Idioglossia
 * "using Specification DSL to write unit tests, as in should, must, not, be"
 * "using Specs2RouteTest DSL to chain HTTP requests for unit testing, as in ~>"
 */

@RunWith(classOf[JUnitRunner])
class SwaggerRoutesTests extends ControllerTestCommon with BeforeAndAfterEach {

    behavior of "Swagger routes"

    it should "server docs" in {
        implicit val tid = transid()
        val swagger = new SwaggerDocs(Uri.Path.Empty, "infoswagger.json")
        Get("/docs") ~> sealRoute(swagger.swaggerRoutes) ~> check {
            status shouldBe PermanentRedirect
            header("location").get.value shouldBe "docs/index.html?url=/api-docs"
        }

        Get("/api-docs") ~> sealRoute(swagger.swaggerRoutes) ~> check {
            status shouldBe OK
            responseAs[JsObject].fields("swagger") shouldBe JsString("2.0")
        }
    }
}
