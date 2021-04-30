<template>
  <section class="books">
    <div class="books books__header">
      <h1 class="books-title">
        Жанры
      </h1>
    </div>
    <div class="book-container">
      <el-table
          :data="genres"
          style="width: 100%">
        <el-table-column
            prop="name"
            label="Название жанра">
        </el-table-column>
      </el-table>
    </div>
  </section>
</template>

<script>
import ErrorsHandler from "@/services/ErrorsHandler";
import GenreService from "@/services/GenreService";


export default {
  name: "Genres",
  data: function () {
    return {
      genres: null,
    };
  },
  methods: {
    async loadData() {
      try {
        this.genres = await GenreService.findAll();
      } catch (errorMessage) {
        ErrorsHandler.notifyError(errorMessage);
      }
    },
  },
  created() {
    this.loadData();
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

.books-title {
  font-size: 2rem;
  margin: 5px;
}
</style>
