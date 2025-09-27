package game.engine.entities.items.attacks;

import game.engine.IWorldView;
import game.engine.entities.IEntity;
import game.engine.modules.IMovingGeometryRepresentation;
import game.utility.Rectangle2F;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RectangleSlashTest {

    private RectangleSlash rectangleSlash;
    private IWorldView mockWorldView;
    private IEntity mockUser;
    @SuppressWarnings("FieldCanBeLocal")
    private IMovingGeometryRepresentation mockGeometry;
    private DamageModifier mockModifier;

    @BeforeEach
    void setUp() {
        rectangleSlash = new RectangleSlash();
        mockWorldView = mock(IWorldView.class);
        mockUser = mock(IEntity.class);
        mockGeometry = mock(IMovingGeometryRepresentation.class);
        mockModifier = mock(DamageModifier.class);

        when(mockUser.geometryRepresentation()).thenReturn(mockGeometry);
        when(mockGeometry.getPosition()).thenReturn(new game.utility.Point2F(0, 0));
        when(mockGeometry.getRotation()).thenReturn(0f);

        when(mockModifier.modify(any(Damage.class), any(IDamageable.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void testDamageableEntityGetsDamaged() {
        IDamageable inRange = mock(IDamageable.class, withSettings().extraInterfaces(IEntity.class));
        IEntity inRangeAsEntity = (IEntity) inRange;

        when(mockWorldView.getEntitiesInArea((Rectangle2F) any(), any())).thenReturn(List.of(inRangeAsEntity));

        rectangleSlash.attack(mockWorldView, mockUser, mockModifier);

        verify(inRange).takeDamage(any(Damage.class), eq(mockUser));
    }

    @Test
    void testNonDamageableEntityIsIgnored() {
        IEntity nonDamageable = mock(IEntity.class);
        when(mockWorldView.getEntitiesInArea((Rectangle2F) any(), any())).thenReturn(List.of(nonDamageable));

        rectangleSlash.attack(mockWorldView, mockUser, mockModifier);

        verifyNoInteractions(nonDamageable);
    }

    @Test
    void testMultipleEntitiesHandledCorrectly() {
        IDamageable enemy1 = mock(IDamageable.class, withSettings().extraInterfaces(IEntity.class));
        IDamageable enemy2 = mock(IDamageable.class, withSettings().extraInterfaces(IEntity.class));
        IEntity other = mock(IEntity.class);

        when(mockWorldView.getEntitiesInArea((Rectangle2F) any(), any()))
                .thenReturn(List.of((IEntity) enemy1, (IEntity) enemy2, other));

        rectangleSlash.attack(mockWorldView, mockUser, mockModifier);

        verify(enemy1).takeDamage(any(Damage.class), eq(mockUser));
        verify(enemy2).takeDamage(any(Damage.class), eq(mockUser));
        verifyNoInteractions(other);
    }

    @Test
    void testDamageIsModifiedByModifier() {
        IDamageable enemy = mock(IDamageable.class, withSettings().extraInterfaces(IEntity.class));
        IEntity enemyAsEntity = (IEntity) enemy;

        Damage modifiedDamage = new Damage(DamageType.SLASH, 999);

        when(mockModifier.modify(any(Damage.class), any(IDamageable.class))).thenReturn(modifiedDamage);
        when(mockWorldView.getEntitiesInArea((Rectangle2F) any(), any())).thenReturn(List.of(enemyAsEntity));

        rectangleSlash.attack(mockWorldView, mockUser, mockModifier);

        verify(enemy).takeDamage(eq(modifiedDamage), eq(mockUser));
    }

}
