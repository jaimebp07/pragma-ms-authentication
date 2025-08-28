package co.com.mycompany.r2dbc;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {
    // TODO: change four you own tests

    @InjectMocks
    UserRepositoryAdapter repositoryAdapter;

  

    @Mock
    ObjectMapper mapper;

}
