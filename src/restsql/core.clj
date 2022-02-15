(ns restsql.core
  (:gen-class)
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-response]]
            [restsql.service :refer [exec-sql]]
            [clojure.tools.logging :as log]
            [ring.adapter.jetty :as ring-jetty]
            [clojure.walk :as walk])
  (:import (org.eclipse.jetty.server Server)))

(defn- ex-status [^Throwable t status]
  (log/error (ex-message t) (ex-cause t))
  {:status status :body {:status status :message (ex-message t)}})

(defn wrap-fallback-exception
  [handler]
  (fn
    [request]
    (try
      (handler request)
      (catch Exception e (ex-status e 500)))))

(defroutes app-routes
           (GET "/api/:query-method"
                {query-params :query-params {m :query-method} :route-params}
             {:body (exec-sql m (walk/keywordize-keys query-params)) :status 200})
           (route/not-found {:body {:status 404 :message "你懂的"} :status 404}))

(def handler
  (-> app-routes
      (wrap-defaults api-defaults)
      (wrap-fallback-exception)
      (wrap-json-response)))


(defonce ^:private instance* (atom nil))

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

(defn- destroy!
  "General application shutdown function which should be called once at application shuddown."
  []
  (log/info "Shutting Down ...")
  (stop-web-server!)
  (log/info "Shutdown COMPLETE"))

(defn- init! []
  (.addShutdownHook (Runtime/getRuntime) (Thread. ^Runnable destroy!))
  (log/info "Initialization COMPLETE"))

(defn- start-normally []
  (try
    (init!)
    (start-web-server! handler)
    (catch Throwable e
      (log/error e "Initialization FAILED")
      (System/exit 1))))

(defn -main []
  (start-normally))


