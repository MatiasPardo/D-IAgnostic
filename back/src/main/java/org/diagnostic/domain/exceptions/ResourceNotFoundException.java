package org.diagnostic.domain.exceptions;

@SuppressWarnings("serial")
public class ResourceNotFoundException extends NotFoundException {

    public ResourceNotFoundException(String id, String resource)  {
        super(id, resource);
    }

}
