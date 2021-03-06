import styled from 'styled-components';

const Container = styled.div`
  position: relative;
  display: flex;
  margin-top: 70px;
  width: 57%;
  min-width: 590px;
  margin-left: max(21.5%, 205px);
  height: 700px;
  box-shadow: 0px 4px 10px 10px rgba(0, 0, 0, 0.25);
  border-radius: 20px;
  transform: ${props => props?.active && 'translateX(-25%)'};
  transition: 1s;
`;

export { Container };
