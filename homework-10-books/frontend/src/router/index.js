import Vue from "vue";
import VueRouter from "vue-router";
import Books from "@/views/books/Books";
import NewBook from "@/views/books/NewBook";
import EditBook from "@/views/books/EditBook";
import BookDetail from "@/views/books/BookDetail";
import Authors from "@/views/Authors";
import Genres from "@/views/Genres";

Vue.use(VueRouter);

const routes = [
  {
    path: "/books",
    name: "Books",
    component: Books,
  },
  {
    path: "/books/new",
    name: "NewBook",
    component: NewBook,
  },
  {
    path: "/books/edit/:id",
    name: "EditBook",
    component: EditBook,
    props: true,
  },
  {
    path: "/books/detail/:id",
    name: "Book",
    component: BookDetail,
    props: true,
  },
  {
    path: "/authors",
    name: "Authors",
    component: Authors,
  },
  {
    path: "/genres",
    name: "Genres",
    component: Genres,
  },
  {
    path: "/",
    redirect: "/books",
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
