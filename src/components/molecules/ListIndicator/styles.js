import styled from 'styled-components';

const Container = styled.div`
  display: flex;
  justify-content: flex-start;
  align-items: center;
  height: 50px;
  width: 100%;
  margin: auto 0 75px 0;
`;

const Label = styled.label`
  position: relative;
  width: 70px;
  height: 35px;
`;

const Switch = styled.input`
  display: none;
`;

const Slider = styled.span`
  display: inline-block;
  width: 100%;
  height: 100%;
  position: relative;
  cursor: pointer;
  background-color: black;
  border-radius: 34px;
  transition: 0.4s;
  border: 1px solid #f9cf00;
  &::before {
    transform: ${props =>
      props.listType === 'discovery' ? 'translateX(35px)' : ''};
    position: absolute;
    content: '';
    height: 30px;
    width: 30px;
    left: 3px;
    bottom: 3px;
    background-color: #f9cf00;
    transition: 0.5s;
    border-radius: 50%;
  }
`;
const buttonStyle = {
  color: 'white',
  padding: '0 15px',
  fontWeight: 'bold',
  fontSize: '20px',
};

export { Container, Slider, Switch, Label, buttonStyle };
