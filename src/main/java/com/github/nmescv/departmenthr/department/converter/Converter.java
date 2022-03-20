package com.github.nmescv.departmenthr.department.converter;

/**
 *
 * @param <Entity> сущность
 * @param <Dto> DTO - отображается в интерфейсе
 */
public interface Converter<Entity, Dto> {

    /**
     *
     * @param dto есть dto
     * @return преобразовання сущность для сохранения, обновления и т.д.
     */
    Entity toEntity(Dto dto);

    /**
     *
     * @param entity используемая сущность
     * @return преобразованный в объект dto с информацией от сущности 
     */
    Dto toDto(Entity entity);
}
