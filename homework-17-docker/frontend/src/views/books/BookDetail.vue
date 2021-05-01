<template>
  <div class="book-detail"  v-if="isNotEmptyBook">
    <book-view class="book-detail__book" :book="book">
      <el-button type="text" @click="back()">Закрыть</el-button>
    </book-view>
    <comments :book-id="id" @back="back"/>
  </div>
</template>

<script>
import BookService from "@/services/BookService";
import ErrorsHandler from "@/services/ErrorsHandler";
import BookView from "@/components/books/BookView";
import Comments from "@/views/books/Comments";

export default {
  name: "EditBook",
  props: ["id"],
  data: function () {
    return {
      book: null,
    };
  },
  components: {
    BookView,
    Comments,
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
    async loadBook() {
      try {
        this.book = await BookService.findById(this.id);
      } catch (error) {
        ErrorsHandler.notifyError(error);
      }
    },
    back() {
      this.$router.push({name: "Books"});
    },
  },
  created() {
    this.loadBook();
  },
};
</script>
<style scoped>
.book-detail {
  display: flex;
  flex-flow: row wrap;
  justify-content: center;
  align-items: stretch;
}

.book-detail__book {
  width: 25%;
}
</style>
