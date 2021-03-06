import React, { useEffect } from 'react';
import propTypes from 'prop-types';
import { Input, NormalButton, TextButton } from '../../atoms';
import {
  Container,
  H1,
  InputListContainer,
  InputContainer,
  Span,
  inputStyle,
  inputStyle2,
  normalButtonStyle,
  Explanation,
  Explain,
  CheckBox,
  CheckBoxContainer,
  textButtonStyle,
  textButtonStyle2,
} from './styles';
import SignInfoHook from './SignInfoHook';
import { AgreementRule, idRule, nickNameRule, pwRule } from './regexp';
import { shallowEqual, useDispatch, useSelector } from 'react-redux';
import {
  idExsistingCheck,
  nickExsistingCheck,
  requestSignUp,
} from '../../../store/SignUp/action';

function SignInfoForm({ modeDispatch }) {
  const {
    idExist,
    nickExist,
    idCheckSuccess,
    nickCheckSuccess,
    checkedId,
    checkedNick,
    name,
    phone,
    birth,
    auth,
    email,
    signUpSuccess,
  } = useSelector(
    state => ({
      idExist: state.signUpReducer.idExist,
      nickExist: state.signUpReducer.nickExist,
      idCheckSuccess: state.signUpReducer.idCheckSuccess,
      nickCheckSuccess: state.signUpReducer.nickCheckSuccess,
      checkedId: state.signUpReducer.inputs.id,
      checkedNick: state.signUpReducer.inputs.nickName,
      name: state.signUpReducer.inputs.name,
      phone: state.signUpReducer.inputs.phone,
      birth: state.signUpReducer.inputs.birth,
      auth: state.signUpReducer.inputs.auth,
      email: state.signUpReducer.inputs.email,
      signUpSuccess: state.signUpReducer.signUpSuccess,
    }),
    shallowEqual,
  );
  const dispatch = useDispatch();
  const Hook = SignInfoHook();
  const {
    id,
    pw,
    pwCheck,
    nickName,
    firstAgreement,
    secondAgreement,
    idError,
    pwError,
    pwCheckError,
    nickNameError,
    firstAgreementError,
    secondAgreementError,
    idCheckError,
    nickNameCheckError,
  } = Hook.state;
  console.log(modeDispatch);

  useEffect(() => {
    if (signUpSuccess) {
      modeDispatch.onSuccessSignUp();
    }
    return () => {
      Hook.onReset();
    };
  }, [signUpSuccess]);

  const idCheckExist = () => {
    if (!idRule(id)) {
      Hook.setError('id');
      return;
    }
    dispatch(idExsistingCheck(id));
  };
  const nickNameCheckExist = () => {
    if (!nickNameRule(nickName)) {
      Hook.setError('nickName');
      return;
    }
    dispatch(nickExsistingCheck(nickName));
  };
  const onSubmit = () => {
    const chkType = [
      'id',
      'pw',
      'nickName',
      'firstAgreement',
      'secondAgreement',
    ];
    const chkValue = [id, pw, nickName, firstAgreement, secondAgreement];
    const chkFunc = [
      idRule,
      pwRule,
      nickNameRule,
      AgreementRule,
      AgreementRule,
    ];
    const result = chkValue
      .map((chk, idx) => {
        if (!chkFunc[idx](chk)) {
          console.log('???????????? ?????? ??????234');
          Hook.setError(chkType[idx]);
          return false;
        }
        if (chkType[idx] === 'pw' && pw !== pwCheck) {
          console.log('???????????? ?????? ??????');
          Hook.setError('pwCheck');
          return false;
        }
        if (chkType[idx] === 'pw' && pw === pwCheck) Hook.resetError('pwCheck');
        else Hook.resetError(chkType[idx]);
        return true;
      })
      .every(chk => chk);
    if (
      result &&
      !idExist &&
      !nickExist &&
      idCheckSuccess &&
      nickCheckSuccess
    ) {
      if (checkedId !== id) {
        Hook.setError('idCheck');
      } else if (checkedNick !== nickName) {
        Hook.setError('nickNameCheck');
      } else {
        dispatch(
          requestSignUp({
            id: checkedId,
            pw,
            auth,
            birth,
            email,
            name,
            phoneNumber: phone,
            nickName: checkedNick,
          }),
        );
      }
    }
  };
  console.log(idCheckSuccess + '*****');
  return (
    <Container>
      <H1>????????????</H1>
      <InputListContainer>
        <Span>?????????</Span>
        <InputContainer>
          <Input
            Style={inputStyle2}
            name="id"
            value={id}
            onChange={Hook.onChange}
          />
          <TextButton Style={textButtonStyle} onClick={idCheckExist}>
            ????????????
          </TextButton>
        </InputContainer>
      </InputListContainer>
      <Explain Error={idCheckSuccess}>
        <Explanation>?????? ????????? ????????? ?????????.</Explanation>
      </Explain>
      <Explain Error={idError}>
        <Explanation>????????? ???????????? ????????????.</Explanation>
      </Explain>
      <Explain Error={idCheckError}>
        <Explanation>??????????????? ???????????????.</Explanation>
      </Explain>
      <Explain Error={idExist}>
        <Explanation>?????? ???????????? ????????? ?????????.</Explanation>
      </Explain>
      <Explain Explain={true}>
        <Explanation>*6~12??? ??????, ????????? ??????????????????.</Explanation>
      </Explain>
      <InputListContainer>
        <Span>????????????</Span>
        <InputContainer>
          <Input
            type="password"
            Style={inputStyle}
            name="pw"
            value={pw}
            onChange={Hook.onChange}
          />
        </InputContainer>
      </InputListContainer>
      <Explain Error={pwError}>
        <Explanation>????????? ???????????? ????????????.</Explanation>
      </Explain>
      <InputListContainer>
        <Span>
          ????????????
          <br />
          ??????
        </Span>
        <InputContainer>
          <Input
            type="password"
            Style={inputStyle}
            name="pwCheck"
            value={pwCheck}
            onChange={Hook.onChange}
          />
        </InputContainer>
      </InputListContainer>
      <Explain Error={pwCheckError}>
        <Explanation>??????????????? ?????? ????????????.</Explanation>
      </Explain>
      <Explain Explain={true}>
        <Explanation>
          *??????????????? ??????, ??????, ?????? ??? ????????? ????????? ???????????? 8??? ????????????
          ??????????????????
        </Explanation>
      </Explain>
      <InputListContainer>
        <Span>?????????</Span>
        <InputContainer>
          <Input
            Style={inputStyle2}
            name="nickName"
            value={nickName}
            onChange={Hook.onChange}
          />
          <TextButton Style={textButtonStyle} onClick={nickNameCheckExist}>
            ????????????
          </TextButton>
        </InputContainer>
      </InputListContainer>
      <Explain Error={nickCheckSuccess}>
        <Explanation>?????? ????????? ????????? ?????????.</Explanation>
      </Explain>
      <Explain Error={nickNameError}>
        <Explanation>????????? ???????????? ????????????.</Explanation>
      </Explain>
      <Explain Error={nickNameCheckError}>
        <Explanation>??????????????? ???????????????.</Explanation>
      </Explain>
      <Explain Error={nickExist}>
        <Explanation>?????? ???????????? ????????? ?????????.</Explanation>
      </Explain>
      <Explain Explain={true}>
        <Explanation>*2-8??? ????????? ??????????????????</Explanation>
      </Explain>
      <CheckBoxContainer>
        <CheckBox checked={firstAgreement}>
          <Input
            type="checkbox"
            Style={{ display: 'none' }}
            name="firstAgreement"
            onChange={Hook.checkBoxChange}
          />
        </CheckBox>
        ??????????????? ???????????????.
        <TextButton Style={textButtonStyle2}>????????????</TextButton>
      </CheckBoxContainer>
      <CheckBoxContainer>
        <CheckBox checked={secondAgreement}>
          <Input
            type="checkbox"
            Style={{ display: 'none' }}
            name="secondAgreement"
            onChange={Hook.checkBoxChange}
          />
        </CheckBox>
        ???????????? ??????/????????? ???????????????.
        <TextButton Style={textButtonStyle2}>????????????</TextButton>
      </CheckBoxContainer>
      <Explain Error={firstAgreementError || secondAgreementError}>
        <Explanation>????????? ???????????????.</Explanation>
      </Explain>
      <InputListContainer>
        <NormalButton Style={normalButtonStyle} onClick={onSubmit}>
          ??????
        </NormalButton>
      </InputListContainer>
    </Container>
  );
}

SignInfoForm.propTypes = {
  isOpen: propTypes.bool,
  modeDispatch: propTypes.object,
};
export default SignInfoForm;
