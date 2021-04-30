import Errors from "@/services/ErrorsHandler";

/**
 * Сервис для работы с коментариями
 */
const URL = `/api/books`;
const CONTENT_TYPE = "application/json;charset=utf-8";
export default {
  /**
   * Загрузка коментариев для книги
   * @returns {Promise<unknown>}
   */
  async findAll(bookId) {
    let url = `${URL}/${bookId}/comments`;
    let response = await fetch(url);
    if (response.ok) {
      let json = await response.json();
      return Promise.resolve(json);
    }
    return Promise.reject(
      Errors.getErrorByCode(response.statusCode, response.text())
    );
  },
  /**
   * Загрузка комментария
   * @returns {Promise<unknown>}
   */
  async findById(bookId, commentId) {
    let response = await fetch(`${URL}/${bookId}/comments/${commentId}`);
    if (response.ok) {
      let json = await response.json();
      return Promise.resolve(json);
    }
    return Promise.reject(
      Errors.getErrorByCode(response.statusCode, response.text())
    );
  },
  /**
   * Удаление комментария
   * @param bookId
   * @param commentId
   * @returns {Promise<void>}
   */
  async remove(bookId, commentId) {
    let response = await fetch(`${URL}/${bookId}/comments/${commentId}`, {
      method: "DELETE",
    });
    if (response.ok) {
      return Promise.resolve();
    }
    return Promise.reject(
      Errors.getErrorByCode(response.statusCode, response.text())
    );
  },
  /**
   * Сохранение комментария
   * @param bookId
   * @param comment
   * @returns {Promise<void>}
   */
  async save(bookId, comment) {
    let url = `${URL}/${bookId}/comments`;
    let method = 'PUT';
    if(comment.id == null)
      method = 'POST';
    let response = await fetch(url, {
      method: method,
      headers: {
        "Content-Type": CONTENT_TYPE,
      },
      body: JSON.stringify(comment),
    });
    if (response.ok === true) {
      let comment = await response.json();
      return Promise.resolve(comment);
    }

    return Promise.reject(
      Errors.getErrorByCode(response.statusCode, response.text())
    );
  },
};
