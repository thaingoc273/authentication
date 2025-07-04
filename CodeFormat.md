## Code format
### Using  Spotless Maven Plugin 
Setting
```java
<plugin>
    <groupId>com.diffplug.spotless</groupId>
    <artifactId>spotless-maven-plugin</artifactId>
    <version>2.44.5</version>
    <executions>
        <execution>
            <goals>
                <goal>check</goal>
                <goal>apply</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <java>
            <googleJavaFormat>
                <version>1.27.0</version>
            </googleJavaFormat>
        </java>
    </configuration>
</plugin>
```

How to run
```java
mvn spotless:check
mvn spotless:apply
```