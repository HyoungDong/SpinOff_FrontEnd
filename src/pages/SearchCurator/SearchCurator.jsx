import React, { useEffect, useRef } from 'react';
import { useDispatch } from 'react-redux';
import { useParams } from 'react-router-dom';
import { SearchCuratorContents } from '../../components/organisms';
import { memberType } from '../../store/SearchFilter/action';
import { Container } from './styles';

function SearchCurator() {
  const rootRef = useRef(null);
  const targetRef = useRef(null);
  const ref = useRef({ rootRef, targetRef });
  const { keyword } = useParams();
  const dispatch = useDispatch();
  useEffect(() => {
    dispatch(memberType());
  }, []);
  return (
    <Container ref={rootRef}>
      <SearchCuratorContents ref={ref} keyword={keyword} />
      <div ref={targetRef} />
    </Container>
  );
}

export default SearchCurator;