package dev.morphia.issue172;


import dev.morphia.TestBase;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.entities.SimpleEnum;
import org.bson.types.ObjectId;
import org.junit.Ignore;
import org.junit.Test;

import java.io.Serializable;

import static dev.morphia.query.experimental.filters.Filters.eq;


public class NameValuePairTest extends TestBase {

    @Test
    @Ignore("add back when TypeLiteral support is in; issue 175")
    public void testNameValuePairWithDoubleIn() {
        getMapper().map(NameValuePairContainer.class);
        final NameValuePairContainer container = new NameValuePairContainer();
        container.pair = new NameValuePair<>(SimpleEnum.FOO, 1.2d);
        getDs().save(container);

        getDs().find(NameValuePairContainer.class)
               .filter(eq("_id", container.id))
               .first();
    }

    @Entity
    private static class NameValuePair<T1 extends Enum<?>, T2> implements Serializable {
        private T2 value;
        private T1 name;

        public NameValuePair() {
        }

        NameValuePair(T1 name, T2 value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final NameValuePair other = (NameValuePair) obj;
            if (name == null) {
                if (other.name != null) {
                    return false;
                }
            } else if (!name.equals(other.name)) {
                return false;
            }
            if (value == null) {
                return other.value == null;
            } else return value.equals(other.value);
        }

    }

    @Entity
    private static class NameValuePairContainer {
        @Id
        private ObjectId id;
        private NameValuePair<SimpleEnum, Double> pair;
    }

}
