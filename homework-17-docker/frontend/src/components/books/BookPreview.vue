<template>
  <el-card class="box-card">
    <div class="book-card__header">
      <h5>{{ bookName }}</h5>
      <el-popconfirm title="Вы действительно желаете удалить книгу?" @confirm="deleteBook">
        <el-button slot="reference" class="book-card__icon-delete" type="text"
          icon="el-icon-delete"
        ></el-button>
      </el-popconfirm>
    </div>
    <div class="book-card__genre">{{ genre }}</div>
    <div class="book-card__authors">
      <div v-for="(author,index) in authors" :key="index">
        {{ author.name }}
      </div>
    </div>
    <div class="book-card__footer">
      <el-button @click="changeBook()">Изменить книгу</el-button>
      <el-button @click="bookDetail()">О книге</el-button>
    </div>
  </el-card>
</template>

<script>
export default {
  name: "BookPreview",
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
    genre: function () {
      return this.book.genre.name;
    },
    authors: function () {
      return this.book.authors;
    },
  },
  methods: {
    deleteBook(){
      this.$emit('delete-book', this.book.id);
    },
    changeBook() {
      this.$emit('change-book', this.book.id);
    },
    bookDetail(){
      this.$emit('detail-book', this.book.id);
    }
  },
};
</script>
<style scoped>
.box-card {
  position: relative;
  width: 480px;
  min-height: 250px;
  display: flex;
  flex-flow: column;
  justify-content: space-between;
}

.book-card__header {
  display: flex;
  flex-flow: row nowrap;
  justify-content: space-between;
  flex-grow: 2;
}

.book-card__icon-delete {
  float: right;
  color: grey;
}

.book-card__genre {
  color: grey;
  text-align: left;
}

.book-card__authors {
  text-align: left;
}

.book-card__footer {
  position: absolute;
  bottom: 10px;
  margin-top: 2rem;
  display: flex;
  flex-flow: row nowrap;
  justify-content: flex-start;
}
</style>
