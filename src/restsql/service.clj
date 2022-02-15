(ns restsql.service
  (:require [restsql.db :as db ]))

(db/def-db-fns "queries.sql")

(defn exec-sql [fname params]
  (let [res (db/with-hug 'restsql.service fname params)]
    (if (number? res)
      {:res res}
      res)))