<template>
  <div class="book-container" v-loading="isEmptyBook">
    <book-form
      v-if="isNotEmptyBook"
      :book="book"
      :genres="genres"
      :authors="authors"
      @saved="saveBook"
      @cancel="back"
      card-title="Редактирование книги"
    />
  </div>
</template>

<script>
import BookService from "@/services/BookService";
import AuthorService from "@/services/AuthorService";
import GenreService from "@/services/GenreService";
import ErrorsHandler from "@/services/ErrorsHandler";
import BookForm from "@/components/books/BookForm";

export default {
  name: "EditBook",
  props: ["id"],
  data: function () {
    return {
      book: null,
      genres: [],
      authors: [],
    };
  },
  components: {
    BookForm,
  },
  computed: {
    isEmptyBook() {
      return this.book === null;
    },
    isNotEmptyBook() {
      return !(this.book === null);
    },
  },
  methods: {
    async loadAuthors() {
      try {
        this.authors = await AuthorService.findAll();
      } catch (error) {
        ErrorsHandler.notifyError(error);
      }
    },
    async loadGenres() {
      try {
        this.genres = await GenreService.findAll();
      } catch (error) {
        ErrorsHandler.notifyError(error);
      }
    },
    async loadBook() {
      try {
        this.book = await BookService.findById(this.id);
      } catch (error) {
        ErrorsHandler.notifyError(error);
      }
    },
    async saveBook() {
      this.$notify({
        title: 'Успешно',
        message: 'Книга успешно изменена',
        type: 'success'
      });
      this.back();
    },
    back() {
      this.$router.push({ name: "Books" });
    },
  },
  created() {
    this.loadBook();
    this.loadAuthors();
    this.loadGenres();
  },
};
</script>
<style scoped>
.book-container {
  display: flex;
  flex-flow: row wrap;
  justify-content: center;
  align-items: stretch;
}
</style>
