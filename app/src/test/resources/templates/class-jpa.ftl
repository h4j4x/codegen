package ${packageName};

@Entity
@Table(name = "${tableName}")
public class ${className} {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
