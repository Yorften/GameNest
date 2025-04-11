import { FormEvent, ChangeEvent, useState, useEffect } from 'react';
import { Modal, Button, Label, TextInput, Textarea } from 'flowbite-react';
import { useAppDispatch, useAppSelector } from '../app/hooks';
import {
    createCategory,
    selectCategoryToEdit,
    setCategoryToEdit,
    Category,
    updateCategory,
} from '../features/categories/categorySlice';
import { toast } from 'sonner';

interface CategoryModalProps {
    isOpen: boolean;
    onClose: () => void;
}

export function CategoryModal({ isOpen, onClose }: CategoryModalProps) {
    const dispatch = useAppDispatch();
    const category = useAppSelector(selectCategoryToEdit);

    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [nameError, setNameError] = useState('');
    const [descError, setDescError] = useState('');
    const [serverError, setServerError] = useState('');

    const handleNameChange = (e: ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        setName(value);
        if (!value.trim()) {
            setNameError('Category name is required.');
        } else {
            setNameError('');
        }
    };

    const handleDescChange = (e: ChangeEvent<HTMLTextAreaElement>) => {
        const value = e.target.value;
        setDescription(value);
        if (!value.trim()) {
            setDescError('Category description is required.');
        } else {
            setDescError('');
        }
    };

    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();

        let invalid = false;
        if (!name.trim()) {
            setNameError('Category name is required.');
            invalid = true;
        }
        if (!description.trim()) {
            setDescError('Category description is required.');
            invalid = true;
        }
        if (invalid) return;

        try {
            if (category) {
                // Update
                await dispatch(updateCategory({ id: category.id, name, description })).unwrap();
                toast.success('Category updated successfully');
                dispatch(setCategoryToEdit(null));
            } else {
                // Create
                await dispatch(createCategory({ name, description })).unwrap();
                toast.success('Category created successfully');
            }

            // Reset
            setName('');
            setDescription('');
            setNameError('');
            setDescError('');
            setServerError('');
            onClose();
        } catch (err: any) {
            console.error(err);
            if (err && err.details && Array.isArray(err.details)) {
                err.details.forEach((detail: { field: string; error: string }) => {
                    if (detail.field === 'name') {
                        setNameError(detail.error);
                    }
                    if (detail.field === 'description') {
                        setDescError(detail.error);
                    }
                });
            } else {
                setServerError(typeof err === 'string' ? err : err.message || 'Request failed');
            }
        }
    };

    useEffect(() => {
        setName(category?.name || '');
        setDescription(category?.description || '');
        setNameError('');
        setDescError('');
        setServerError('');
    }, [category]);

    return (
        <Modal show={isOpen} onClose={onClose}>
            <Modal.Header>
                {category ? 'Edit Category' : 'Create New Category'}
            </Modal.Header>
            <Modal.Body>
                <form onSubmit={handleSubmit} className="space-y-6">
                    {serverError && <p className="text-red-600 text-sm">{serverError}</p>}

                    <div>
                        <Label htmlFor="categoryName" value="Category Name" />
                        <TextInput
                            autoFocus={true}
                            id="categoryName"
                            placeholder="E.g. Platformers"
                            required
                            value={name}
                            onChange={handleNameChange}
                            color={nameError ? 'failure' : ''}
                            helperText={nameError && <span className="text-red-600">{nameError}</span>}
                        />
                    </div>

                    <div>
                        <Label htmlFor="categoryDescription" value="Category Description" />
                        <Textarea
                            id="categoryDescription"
                            placeholder="Describe the category..."
                            required
                            rows={4}
                            value={description}
                            onChange={handleDescChange}
                            color={descError ? 'failure' : ''}
                            helperText={descError && <span className="text-red-600">{descError}</span>}
                        />
                    </div>
                </form>
            </Modal.Body>
            <Modal.Footer>
                <Button onClick={handleSubmit}>
                    {category ? 'Update Category' : 'Create Category'}
                </Button>
                <Button color="gray" onClick={onClose}>
                    Cancel
                </Button>
            </Modal.Footer>
        </Modal>
    );
}
