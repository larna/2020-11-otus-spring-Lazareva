<template>
  <section class="books">
    <div class="books books__header">
      <h1 class="books-title">
        Авторы
      </h1>
    </div>
    <div class="book-container">
      <el-table
          :data="authors"
          style="width: 100%">
        <el-table-column
            prop="name"
            label="Имя автора">
        </el-table-column>
        <el-table-column
            prop="realName"
            label="Настоящее имя">
        </el-table-column>
        <el-table-column
            prop="birthday"
            label="День рождения">
        </el-table-column>
      </el-table>
    </div>
  </section>
</template>

<script>
import ErrorsHandler from "@/services/ErrorsHandler";
import AuthorService from "@/services/AuthorService";

export default {
  name: "Authors",
  data: function () {
    return {
      authors: null,
    };
  },
  methods: {
    async loadData() {
      try {
        this.authors = await AuthorService.findAll();
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
