(ns restsql.db
  (:require [hugsql.core :as hugsql]
            [hikari-cp.core :refer :all]
            [clojure.java.jdbc :as jdbc]
            [clojure.walk :as walk]
            [clojure.tools.logging :as log]))

(hugsql/def-db-fns "queries.sql")

;; 文档地址 https://cljdoc.org/d/hikari-cp/hikari-cp/2.13.0/doc/readme
(def datasource-options {:adapter "h2"
                         :url     "jdbc:h2:~/test"})

(defonce datasource
         (delay (make-datasource datasource-options)))

(defn with-hug [method params]
  (jdbc/with-db-connection
    [conn {:datasource @datasource}]
    (let [method (symbol method)
          res ((ns-resolve
                 'restsql.db (symbol method)) conn (walk/keywordize-keys params))]
      (log/debug "invoke sql res" res)
      (if (number? res)
        {:res res}
        res))))
