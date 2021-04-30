<template>
  <el-form :model="commentForm" :rules="rules" ref="ruleForm" label-position="left">
    <el-form-item label="Комментарий" prop="description">
      <el-input type="textarea" placeholder="Новый комментарий" v-model="commentForm.description"/>
    </el-form-item>
    <el-form-item>
      <el-button icon="el-icon-plus" type="primary" @click="save('ruleForm')">Сохранить</el-button>
      <el-button @click="clear('ruleForm')">Очистить</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import CommentsService from "@/services/CommentsService";
import ErrorsHandler from "@/services/ErrorsHandler";

export default {
  name: "BookCard",
  props: {
    comment: {
      type: Object,
      required: true,
    },
    bookId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      commentForm:{
        description: '',
      },
      rules: {
        description: [
          {required: true, message: 'Комментарий не должен быть пуст', trigger: 'blur'},
        ],
      }
    };
  },
  watch: {
    comment(newValue, oldValue){
      if(newValue === oldValue)
        return;
      this.domainToForm();
    }
  },
  methods: {
    save(formName) {
      this.$refs[formName].validate((valid) => {
        if (!valid)
          return false;
        let comment = this.formToDomain();
        this.saveComment(comment);
      });
    },
    async saveComment(comment) {
      try {
        let savedComment = await CommentsService.save(this.bookId, comment);
        this.$notify({
          title: 'Успешно',
          message: 'Комментарий успешно сохранен',
          type: 'success'
        });
        this.$emit('saved', savedComment);
      } catch (error) {
        ErrorsHandler.notifyError(error);
      }
    },
    clear(formName) {
      this.$refs[formName].resetFields();
    },
    domainToForm() {
      this.commentForm.description = this.comment.description;
    },
    formToDomain() {
      return Object.assign({ id: this.comment.id} , this.commentForm);
    },
  },
  created() {
    this.domainToForm();
  },
};
</script>

<style scoped>
</style>
