(ns guestbook.views
  (:require [hiccup.page :as page]
            [guestbook.db :as db]
            [ring.util.anti-forgery :as util]))

(defn message-component
  "Component for guestbook messages"
  [username message-contents]
  [:div.card
   [:div.card-body
    [:h5.card-title username]
    [:p.card-text message-contents]]])

(defn form-component
  []
  "Component for the guestbook 'leave message' form."
  [:form {:action "/new" :method "POST"}
   (util/anti-forgery-field)
   [:div.form-floating.mb-3
    [:input#name.form-control {:type "text" :maxlength 32 :name "username"}]
    [:label {:for "name"} "Username"]]
   [:div.form-floating.mb-3
    [:textarea#message.form-control {:type "text"
                                     :maxlength 2000
                                     :name "message"
                                     :style "height: 100px"}]
    [:label {:for "message"} "Your message (2000 char max)"]]
   [:input.btn.btn-primary {:type "submit" :value "Send message"}]])


(defn gen-page-head
  [title]
  [:head
   [:title (str "Guestbook - " title)]
   (page/include-css "/css/bootstrap.min.css")
   (page/include-js "/js/bootstrap.min.js")])

(def header-links
 [:nav.navbar.navbar-expand-lg.bg-body-tertiary
 [:div.container-fluid
  [:a.navbar-brand {:href "#"} "Guestbook"]
  [:button.navbar-toggler
   {:type "button"
    :data-bs-toggle "collapse"
    :data-bs-target "#navbarNav"
    :aria-controls "navbarNav"
    :aria-expanded "false"
    :aria-label "Toggle navigation"}
   [:span.navbar-toggler-icon]]
  [:div {:class "collapse navbar-collapse"
         :id "navbarNav"}
   [:ul.navbar-nav
    [:li.nav-item
     [:a.nav-link {:href "/"} "All messages"]]
    [:li.nav-item
     [:a.nav-link { :href "/new"} "Leave your message"]]]]]])

(defn create-message-page
  []
  (page/html5
   (gen-page-head "Create Message")
   header-links
   [:main.m-3
    [:h1.mb-4 "Create Message"]
    (form-component)]))

(defn create-message-results-page
  [{:keys [username message]}]
  (let [{id :GUESTBOOK_DATA/ID} (db/add-message-to-db username message)]
    (page/html5
     (gen-page-head "Create Message")
     header-links
     [:main.m-3
      [:h1.mb-4 "Create Message"]
      [:div.alert.alert-success {:role "alert"} "Thanks for leaving your message!"]
      (form-component)])))

(defn home-page
  []
  (let [all-messages (db/get-all-messages)]
    (page/html5
     (gen-page-head "All pages")
     header-links
     [:div.vstack.gap-2.p-3
      (for [message (reverse all-messages)]
        (message-component (:GUESTBOOK_DATA/USERNAME message)
                           (:GUESTBOOK_DATA/MESSAGE message)))])))
