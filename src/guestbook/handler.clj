(ns guestbook.handler
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [guestbook.views :as views]))

(defroutes app-routes
  (GET "/" [] (views/home-page))
  (GET "/new" [] (views/create-message-page))
  (POST "/new"
        {params :params}
        (views/create-message-results-page params))
  (route/resources "/")
  (route/not-found "The page you were requesting was not found."))

(def app
  (wrap-defaults #'app-routes site-defaults))

(defn -main []
  (jetty/run-jetty #'app {:port 3000}))
