<template>
  <div class="book-container">
    <book-form
      :book="book"
      :genres="genres"
      :authors="authors"
      @saved="saveBook"
      @cancel="redirectToBooks"
      card-title="Новая книга"
    />
  </div>
</template>

<script>
import Book from "@/domain/Book";
import AuthorService from "@/services/AuthorService";
import GenreService from "@/services/GenreService";
import BookForm from "@/components/books/BookForm";
import ErrorsHandler from "@/services/ErrorsHandler";

export default {
  name: "NewBook",
  data: function () {
    return {
      genres: [],
      authors: [],
    }
  },
  components: {
    BookForm,
  },
  computed: {
    isEmptyBooks: function () {
      if (this.books == null)
        return true;
      return false;
    },
    book: function () {
      return new Book();
    },
  },
  methods: {
    async loadAuthors(){
      try {
        this.authors = await AuthorService.findAll();
      }catch(error){
        ErrorsHandler.notifyError(error);
      }
    },
    async loadGenres(){
      try {
        this.genres = await GenreService.findAll();
      }catch(error){
        ErrorsHandler.notifyError(error);
      }
    },
    async saveBook() {
      this.$notify({
        title: 'Успешно',
        message: 'Книга успешно добавлена',
        type: 'success'
      });
      this.redirectToBooks();
    },
    redirectToBooks() {
      this.$router.push({name: "Books"});
    },
  },
  created() {
    this.loadAuthors();
    this.loadGenres();
  }
};
</script>
<style scoped>
.book-container {
  display: flex; /* or inline-flex */
  flex-flow: row wrap;
  justify-content: center;
  align-items: stretch;
}
</style>
