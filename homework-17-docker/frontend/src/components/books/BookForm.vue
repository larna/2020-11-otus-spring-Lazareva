<template>
  <el-card class="book-card">
    <div slot="header" class="book-card__header">
      <span>{{ cardTitle }}</span>
      <el-button type="text" @click="cancel()">Закрыть &times;</el-button>
    </div>
    <el-form :model="bookForm" :rules="rules" ref="ruleForm" label-position="left" class="demo-ruleForm">
      <el-form-item label="Название книги" prop="name">
        <el-input v-model="bookForm.name"></el-input>
      </el-form-item>
      <el-form-item label="ISBN" prop="isbn">
        <el-input v-model="bookForm.isbn"></el-input>
      </el-form-item>
      <el-form-item label="Жанр" prop="genre">
        <el-select v-model="bookForm.genre" placeholder="Выберите жанр" class="book-card__genres">
          <el-option v-for="item in genres" :label="item.name" :value="item.id" :key="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Авторы" prop="authors">
        <el-select v-model="bookForm.authors" placeholder="Выберите авторов" multiple class="book-card__authors">
          <el-option v-for="item in authors" :label="item.name" :value="item.id" :key="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="save('ruleForm')">Сохранить</el-button>
        <el-button @click="cancel()">Отмена</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import BookService from "@/services/BookService";
import ErrorsHandler from "@/services/ErrorsHandler";
const VALIDATE_RULES =  {
  name: [
    {required: true, message: 'Название книги должно быть заполнено', trigger: 'blur'},
  ],
  genre: [
    {required: true, message: 'Жанр должен быть выбран', trigger: 'change'}
  ],
  authors: [
    {required: true, message: 'Авторы должны быть выбраны', trigger: 'change'}
  ],
};
export default {
  name: "BookForm",
  props: {
    cardTitle: {
      type: String,
      required: true
    },
    book: {
      type: Object,
      required: true
    },
    genres: {
      type: Array,
      required: true
    },
    authors: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      bookForm: {
        name: '',
        isbn: '',
        genre: '',
        authors: []
      },
    };
  },
  methods: {
    save(formName) {
      this.$refs[formName].validate((valid) => {
        if (!valid)
          return false;
        this.saveBook(this.formToDomain());
      });
    },
    async saveBook(book) {
      try {
        await BookService.save(book);
        this.$emit('saved', book);
      } catch (error) {
        ErrorsHandler.notifyError(error);
      }
    },
    cancel() {
      this.$emit("cancel");
    },
    domainToForm() {
      let authorsId = this.book?.authors.map(item => item.id);
      this.bookForm = {
        name: this.book.name,
        isbn: this.book.isbn,
        genre: this.book.genre?.id,
        authors: authorsId
      };
    },
    formToDomain() {
      let obj = Object.assign({id: this.book.id}, this.bookForm);
      obj.genre = {id: this.bookForm.genre};
      obj.authors = this.bookForm.authors.map(item => {
        return {id: item}
      });
      return obj;
    },
  },
  created() {
    this.domainToForm();
    this.rules = VALIDATE_RULES;
  },
};
</script>

<style scoped>
.book-card {
  width: 75%;
}

.book-card__header {
  display: flex;
  flex-flow: row nowrap;
  justify-content: space-between;
  flex-grow: 2;
}

.book-card__genres,
.book-card__authors {
  width: 100%;
}
</style>
