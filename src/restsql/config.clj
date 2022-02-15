(ns restsql.config
  (:require[cprop.core :refer [load-config]]
           [cprop.source :refer [from-env from-system-props]]))

(def env
  (load-config
    :merge
    [(from-system-props)
     (from-env)]))