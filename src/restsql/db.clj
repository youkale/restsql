(ns restsql.db
  (:require [hugsql.core :as hugsql]
            [hikari-cp.core :refer :all]
            [clojure.java.jdbc :as jdbc]
            [restsql.config :refer [env]]
            [clojure.tools.logging :as log]))

(def debug? (:sql-debug env))

(defmacro def-db-fns [sql-file]
  `(do (hugsql/def-db-fns ~sql-file)
       (when debug?
          (hugsql/def-sqlvec-fns ~sql-file))))

(defonce datasource
         (delay (make-datasource (:database env))))

(defn with-hug [ns db-fn params]
  (let [fname (symbol db-fn)]
   (jdbc/with-db-connection
     [conn {:datasource @datasource}]
     (let [idbf (ns-resolve ns fname)
           res (idbf conn params)]
       (when debug?
         (let [f (ns-resolve ns (-> (name fname) (str "-sqlvec") symbol))]
           (log/debugf "invoke %s : %s => \n %s" (-> f meta :doc) (f params)  res)))
       res))))
