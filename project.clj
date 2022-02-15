(defproject restsql "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.3"]

                 [compojure "1.6.2"]
                 [ring/ring-jetty-adapter "1.9.4"]
                 [ring/ring-codec "1.2.0"]
                 [ring/ring-json "0.5.1" ]
                 [ring/ring-defaults "0.3.3" :exclusions [[javax.servlet/javax.servlet-api]] :scope "provided"]

                 [cprop "0.1.19"]

                 [ch.qos.logback/logback-classic "1.2.10" ]
                 [org.clojure/tools.logging "1.2.4" ]
                 [com.layerware/hugsql "0.5.1"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [hikari-cp "2.13.0"]

                 ;; 数据驱动, 对应要改restsql/db/datasource-options的配置
                 ;;

                 ;;版本地址: https://mvnrepository.com/artifact/mysql/mysql-connector-java
                 [mysql/mysql-connector-java "8.0.28"]
                 ;;版本地址 https://mvnrepository.com/artifact/org.postgresql/postgresql
                 ;[org.postgresql/postgresql "42.3.1"]
                 ]

  :pom-plugins [[org.apache.maven.plugins/maven-compiler-plugin "3.8.1"
                 (:configuration
                   [:source "1.8"]
                   [:target "1.8"])]
                [com.theoryinpractise/clojure-maven-plugin "1.8.4"
                 (:configuration
                   [:sourceDirectories [:sourceDirectory "src/clojure"]])]]

  :global-vars {*warn-on-reflection* false}

  :jvm-opts
  ["-XX:+IgnoreUnrecognizedVMOptions"
   "-Xverify:none"
   "-Djava.awt.headless=true"
   "-XX:-OmitStackTraceInFastThrow"]

  :main restsql.core
  :aot :all
  :javac-options ["-target" "1.8" "-source" "1.8" "-Xlint:-options"]

  :repl-options {:init-ns restsql.core}

  :profiles
  {
   :dev          [:project/dev]
   :test         [:project/dev :project/test]

   :project/test {:jvm-opts ["-Dconf=config-test.edn"]}

   :project/dev  {:jvm-opts     ["-Dconf=config-dev.edn"]
                  :plugins      [[lein-ring "0.12.6"]]
                  :dependencies [[ring/ring-mock "0.4.0"]
                                 [ring/ring-devel "1.7.1"]
                                 [com.h2database/h2 "1.4.200"]]}
   :uberjar      {:omit-source  true
                  :aot          :all
                  :jvm-opts
                  ["-XX:+IgnoreUnrecognizedVMOptions"
                   "-Xverify:none"
                   "-Djava.awt.headless=true"
                   "-XX:-OmitStackTraceInFastThrow"]
                  :uberjar-name "restsql.jar"}})
