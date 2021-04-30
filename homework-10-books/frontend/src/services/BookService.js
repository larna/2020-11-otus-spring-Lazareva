import Errors from "@/services/ErrorsHandler";
import qs from "querystring";

/**
 * Сервис для работы с api книг
 */
const URL = `/api/books`;
const CONTENT_TYPE = "application/json;charset=utf-8";
export default {
  /**
   * Загрузка книг
   * @returns {Promise<unknown>}
   */
  async findAll(page) {
    let params = qs.stringify({ page });
    let url = `${URL}?${params}`;
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
   * Загрузка книг
   * @returns {Promise<unknown>}
   */
  async findById(bookId) {
    let response = await fetch(`${URL}/${bookId}`);
    if (response.ok) {
      let json = await response.json();
      return Promise.resolve(json);
    }
    return Promise.reject(
      Errors.getErrorByCode(response.statusCode, response.text())
    );
  },
  /**
   * Удаление книги
   * @param bookId
   * @returns {Promise<void>}
   */
  async remove(bookId) {
    let response = await fetch(`${URL}/${bookId}`, {
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
   * Создание книги
   * @param book
   * @returns {Promise<void>}
   */
  async save(book) {
    let method = 'PUT';
    if(book.id == null)
      method = 'POST';

    let response = await fetch(URL, {
      method: method,
      headers: {
        "Content-Type": CONTENT_TYPE,
      },
      body: JSON.stringify(book)
    });
    if (response.ok === true) {
      return Promise.resolve();
    }

    return Promise.reject(
      Errors.getErrorByCode(response.statusCode, response.text())
    );
  },
};
