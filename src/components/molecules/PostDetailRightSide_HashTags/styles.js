import styled from 'styled-components';
import { ReactComponent as more } from '../../../assets/images/tagmore.svg';
import { ReactComponent as del } from '../../../assets/images/tagTrash.svg';

const Container = styled.div`
  position: relative;
  width: 100%;
  height: 10%;
  overflow: ${props => (props.isHidden ? 'hidden' : '')};
`;

const TagContainer = styled.div`
  display: flex;
  flex-wrap: wrap;
  width: 90%;
  box-sizing: border-box;
  align-items: center;
  background: white;
`;

const More = styled(more)`
  position: absolute;
  height: 1rem;
  width: 1rem;
  top: calc(50% - 0.5rem);
  transform: ${props => (props.isHidden ? 'rotate(180deg)' : '')};
  right: 0;
  cursor: pointer;
`;

const ButtonContainer = styled.div`
  position: relative;
  margin: 0.625rem 0;
  margin-right: 0.3125rem;
  border-radius: 1.25rem;
  &:hover div {
    visibility: visible;
    opacity: 1;
    transition: 0.4s;
  }
`;
const ButtonHover = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  position: absolute;
  visibility: hidden;
  border-radius: 1.25rem;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.34);
  transition: 0.4s;
  opacity: 0;
  cursor: pointer;
`;
const DeleteIcon = styled(del)`
  cursor: pointer;
`;
const normalButtonStyle = {
  padding: '.3125rem .625rem',
  borderRadius: '1.25rem',
  background: 'transparent',
  fontWeight: 'bold',
};
export {
  Container,
  TagContainer,
  More,
  normalButtonStyle,
  ButtonContainer,
  ButtonHover,
  DeleteIcon,
};
