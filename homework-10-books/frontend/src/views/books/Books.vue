<template>
  <section class="books">
    <div class="books books__header">
      <h1 class="books-title">
        <i class="el-icon-reading books-title__icon"/> Книги
      </h1>
      <el-button type="success" @click="newBook()">Новая книга</el-button>
    </div>
    <div class="book-container">
      <el-alert type="info" v-if="isEmptyBooks">Список книг пуст</el-alert>
      <book-preview
          class="book-container__book"
          :book="book"
          v-for="book in books"
          :key="book.id"
          @reload="loadData(pageNumber)"
          @change-book="changeBook"
          @detail-book="detailBook"
          @delete-book="deleteBook"
      />
    </div>
    <el-pagination
        v-if="booksPage != null"
        @current-change="changePageNumber"
        :current-page.sync="pageNumber"
        background
        layout="prev, pager, next"
        :page-size="booksPage.size"
        :total="booksPage.totalElements"
        hide-on-single-page
    >
    </el-pagination>
  </section>
</template>

<script>
import ErrorsHandler from "@/services/ErrorsHandler";
import BookService from "@/services/BookService";
import BookPreview from "@/components/books/BookPreview.vue";

export default {
  name: "Books",
  data: function () {
    return {
      booksPage: null,
      pageNumber: 1,
    };
  },
  components: {
    BookPreview,
  },
  computed: {
    isEmptyBooks: function () {
      if (this.books == null || this.books.length == 0) return true;
      return false;
    },
    books: function () {
      if (this.booksPage != null && this.booksPage.content != null)
        return this.booksPage.content;
      return [];
    },
  },
  methods: {
    async loadData(page) {
      try {
        this.booksPage = await BookService.findAll(page - 1);
      } catch (errorMessage) {
        ErrorsHandler.notifyError(errorMessage);
      }
    },
    newBook() {
      this.$router.push({name: "NewBook"});
    },
    changePageNumber(number) {
      this.pageNumber = number;
      this.loadData(this.pageNumber);
    },
    changeBook(bookId) {
      this.$router.push({name: "EditBook", params: {id: bookId}});
    },
    detailBook(bookId) {
      this.$router.push({name: "Book", params: {id: bookId}});
    },
    async deleteBook(bookId) {
      try {
        await BookService.remove(bookId);
        this.$notify.success("Удаление усрешно!");
        this.reload();
      } catch (error) {
        ErrorsHandler.notifyError(error);
      }
    },
    reload() {
      let num = this.pageNumber;
      if (this.books.length == 1){
        num = this.pageNumber == 1 ? 1 : this.pageNumber -1;
      }
      this.changePageNumber(num);
    }
  },
  created() {
    this.loadData(this.pageNumber);
  },
};
</script>
<style scoped>
.books {
  text-align: left;
}

.book-container {
  display: flex; /* or inline-flex */
  flex-flow: row wrap;
  justify-content: flex-start;
  align-items: stretch;
}

.books.books__header {
  margin-bottom: 15px;
}

.book-container__book {
  margin-bottom: 4rem;
  align-self: flex-start;
  margin-right: 30px;
}

.books-title {
  font-size: 2rem;
  margin: 5px;
}

.books-title__icon {
  color: grey;
}
</style>
