package hyve.petshow.controller.converter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.*;
import java.util.stream.Collectors;

public interface Converter<T, Y> {
    Y toRepresentation(T domain);
    T toDomain( Y representation);
    
    
    default List<Y> toRepresentationList(List<T> domainList) {
    	return Optional.ofNullable(domainList).map(lista -> {
    		return lista.stream().map(el -> toRepresentation(el)).collect(Collectors.toList());
    	}).orElse(new ArrayList<Y>());
    }
    
    default List<T> toDomainList(List<Y> representationList) {
    	return Optional.ofNullable(representationList).map(lista -> {
    		return lista.stream().map(el -> toDomain(el)).collect(Collectors.toList());
    	}).orElse(new ArrayList<T>());
    }
    
    default List<Y> toRepresentationList(Set<T> domainList) {
    	return Optional.ofNullable(domainList).map(lista -> {
    		return lista.stream().map(el -> toRepresentation(el)).collect(Collectors.toList());
    	}).orElse(new ArrayList<Y>());
    }
    
    default Set<T> toDomainSet(List<Y> representationList) {
    	return Optional.ofNullable(representationList).map(lista -> {
    		return lista.stream().map(el -> toDomain(el)).collect(Collectors.toSet());
    	}).orElse(Collections.emptySet());
    }

    default Page<Y> toRepresentationPage(Page<T> domainPage){
        return new PageImpl<>(toRepresentationList(domainPage.getContent()), domainPage.getPageable(),
                domainPage.getTotalElements());
    }
}
