<template>
  <el-card class="book" shadow="never">
      <h2>{{ bookName }}</h2>
      <div class="book__content">
      <div class="book__isbn"><strong>ISBN:</strong> {{ isbn }}</div>
      <div class="book__genre">{{ genre }}</div>
      <div class="book__authors">
        <div v-for="(author, index) in authors" :key="index">
          {{ author.name }}
        </div>
      </div>
    </div>
    <div class="book__footer">
    <slot></slot>
    </div>
  </el-card>
</template>

<script>
export default {
  name: "BookView",
  props: {
    book: {
      type: Object,
      required: true,
    },
  },
  computed: {
    bookName: function () {
      return this.book.name;
    },
    isbn: function () {
      return this.book.isbn;
    },
    genre: function () {
      return this.book.genre.name;
    },
    authors: function () {
      return this.book.authors;
    },
  },
};
</script>
<style scoped>
.book{
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.book__content {
  display: flex;
  flex-direction: column;
  align-content: flex-start;
  align-items: baseline;
}
.book__isbn,
.book__genre {
  color: grey;
  text-align: left;
  padding: 10px;
}
.book__authors {
  text-align: left;
  padding: 10px;
}
.book__footer{
  position: absolute;
  bottom: 10px;
}
</style>
