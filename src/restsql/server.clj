(ns restsql.server
  (:require
    [compojure.core :refer :all]
    [ring.adapter.jetty :as ring-jetty]
    [ring.middleware.json :refer [wrap-json-response]]
    [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
    [clojure.tools.logging :as log])
  (:import (org.eclipse.jetty.server Server)))

(defonce ^:private instance* (atom nil))

(defn instance []
  @instance*)

(defn start-web-server!
  [handler]
  (when-not @instance*
    (let [opts {:port 3000 :send-server-version? false :join? true}
          new-server (ring-jetty/run-jetty handler opts)]
      (compare-and-set! instance* nil new-server)
      :started)))

(defn stop-web-server!
  []
  (let [[^Server old-server] (reset-vals! instance* nil)]
    (when old-server
      (log/info "Shutting Down Embedded Jetty WebServer")
      (.stop old-server)
      :stopped)))
