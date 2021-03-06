import { all, call } from 'redux-saga/effects';
import { watchSignUp } from './SignUp/sagas';
import { watchSignIn } from './SignIn/saga';
import { watchCreatePost } from './Post/saga';

export default function* rootSaga() {
  yield all([call(watchSignUp), call(watchSignIn), call(watchCreatePost)]);
}
