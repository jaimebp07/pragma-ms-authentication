package co.com.mycompany.usecase.getlistusers;

import java.util.Set;
import java.util.UUID;

import co.com.mycompany.model.exceptions.BusinessException;
import co.com.mycompany.model.exceptions.ErrorCode;
import co.com.mycompany.model.gateways.UserRepositoryGateway;
import co.com.mycompany.model.user.User;
import reactor.core.publisher.Mono;

public class GetListUsersUseCase {

    private final UserRepositoryGateway userRepositoryGateway;

    public GetListUsersUseCase(UserRepositoryGateway userRepositoryGateway){
        this.userRepositoryGateway = userRepositoryGateway;
    }

    public Mono<Set<User>> getListUsers(Set<UUID> idList){
        if(idList == null || idList.isEmpty()){
            return Mono.error(new BusinessException(
                ErrorCode.INVALID_ARGUMENT,
                "It is required that there is at least one client id in the list"));
        }
        return userRepositoryGateway.findByIdList(idList).flatMap(users  -> 
            {
                if(users  == null || users .isEmpty()) {
                    return Mono.error(new BusinessException(
                        ErrorCode.USER_NOT_FOUND, 
                        "No users were found for the provided IDs"));
                }
                
                return Mono.just(users );
            }
        );
    }
}
